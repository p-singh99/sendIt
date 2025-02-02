import { app, BrowserWindow } from 'electron';
import path from 'path';
import { isDev } from './utils.js';
import Peer from './peer/Peer.js';

const appPathRoot = app.getAppPath();

app.on("ready", () => {
    const mainWindow = new BrowserWindow({});
    
    if(isDev()) {
        mainWindow.loadURL('http://localhost:5140');
    } else {
        mainWindow.loadFile(path.join(appPathRoot, '/dist-react/index.html'));
    }
});

const peer = new Peer(6000);
peer.listen();