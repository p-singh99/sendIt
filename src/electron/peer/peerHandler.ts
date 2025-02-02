import { Socket } from "net"
import { PeerComms } from "../constants/PeerComms.js";
import Device from "../types/Device.js";
import { ip } from "address";
import { hostname } from 'os';

export const peerHandler = (soc: Socket) => {
    soc.on('data', (data) => {
        const message = data.toString();
        
        switch(message) {
            case PeerComms.HELLO: {
                 
                const device: Device = {
                    ip: ip()!,
                    name: hostname(),
                    type: 'device'
                }
                soc.write(PeerComms.HELLO + ' ' + JSON.stringify(device));
                break;
            }
        }
    });
}