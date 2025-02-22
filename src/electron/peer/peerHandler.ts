import { Socket } from "net"
import { PeerComms } from "../constants/PeerComms.js";
import Device from "../types/Device.js";
import { ip } from "address";
import { AppSettings } from "../constants/AppSettings.js";
import { getDeviceType, getHostName } from "../utils.js";

export const peerHandler = (soc: Socket) => {
    soc.on('data', (data) => {
        const message = data.toString();
        
        switch(message) {
            case PeerComms.HELLO: {
                 
                const device: Device = {
                    ip: ip()!,
                    name: getHostName(),
                    type: getDeviceType()
                }
                soc.write(PeerComms.HELLO + AppSettings.DELIMITER_STRING + JSON.stringify(device));
                break;
            }
        }
    });
}