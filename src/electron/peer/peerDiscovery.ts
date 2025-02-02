import { ip } from 'address';
import Device from '../types/Device.js';
import { getSubnet } from '../utils.js';
import net from 'net';
import { PeerComms } from '../constants/PeerComms.js';

export async function scan(): Promise<Device[]> {
    console.log('Scanning available devices ...');
    
    const devices: Device[] = [];
    const promises = [];
    const localIP = ip();

    if (ip !== undefined) {
        const subnet = getSubnet(localIP!);

        for (let i = 0; i < 255; i++) {
            const host = subnet + "." + i;
            
            if (host !== localIP) {
                promises.push(isReachable(host));
            }
        }

        const results = await Promise.allSettled(promises);
        results.forEach((result) => {
            if (result.status !== 'rejected') {
                devices.push(result.value);
            }
        });
    }
    
    return devices;
}


function isReachable(peer: string): Promise<Device> {
    return new Promise<Device>((resolve, reject) => {
        const socket = new net.Socket();
        socket.connect(5140, peer, () => {
            socket.write(PeerComms.HELLO);
        });

        socket.on('data', (response) => {
            console.log('Received response from host: ' + response);
            const deviceResponse: Device = JSON.parse(response.toString());
           
            resolve(deviceResponse);
        });

        socket.on('error', () => {
            reject(new Error('Unable to reach peer @ ' + peer));
        });
    });
}