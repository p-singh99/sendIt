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
        console.log('Server is listening ...');
        const server = net.createServer()
        .listen(this.port)
        .on('connection', socket => peerHandler(socket));
    }
}