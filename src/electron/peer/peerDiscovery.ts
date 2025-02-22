import { ip } from 'address';
import Device from '../types/Device.js';
import { getSubnet } from '../utils.js';
import net from 'net';
import { PeerComms } from '../constants/PeerComms.js';
import { AppSettings } from '../constants/AppSettings.js';

export async function scan(): Promise<Device[]> {
    console.log('Scanning available devices ...');
    
    const devices: Device[] = [];
    const promises = [];
    const localIP = ip();

    if (ip !== undefined) {
        const subnet = getSubnet(localIP!);

        for (let i = 0; i < 101; i++) {
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
        socket.setTimeout(500);

        socket.connect(6000, peer, () => {
            socket.write(PeerComms.HELLO);
        });

        socket.on('timeout', () => {
            console.log('Connection Timed out for ' + peer);
            reject(new Error('Unable to reach peer @ ' + peer));
        })

        socket.on('data', (response) => {
            console.log('Received response from host: ' + response);

            const deviceInfo = response.toString().split(AppSettings.DELIMITER_STRING)[1];
            console.log(response.toString().split(' ')[1]);
            const deviceResponse: Device = JSON.parse(deviceInfo);
           
            resolve(deviceResponse);
        });

        socket.on('error', () => {
            reject(new Error('Unable to reach peer @ ' + peer));
        });
    });
}