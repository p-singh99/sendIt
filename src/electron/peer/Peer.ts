import net from 'net';
import { peerHandler } from './peerHandler.js';

export default class Peer {
    port: number;

    public constructor(port: number) {
        this.port = port;
    }

    /**
     * Start listening for incoming connections
     */
    listen = (): void => {
        const server = net.createServer()
        .listen(this.port, '127.0.0.1')
        .on('connection', socket => peerHandler(socket));
    }
}