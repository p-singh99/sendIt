import cp from "child_process";
import os from 'os';

export function isDev(): boolean {
    return process.env.NODE_ENV === 'dev';
}

export function getSubnet(ip: string): string {
    return ip.slice(0, ip.lastIndexOf('.'));
}

export function getHostName(): string {
    const platform = os.platform();

    switch(platform) {
        case "win32":
            return process.env.COMPUTERNAME!;
        case "darwin":
            return cp.execSync("scutil --get ComputerName").toString().trim();
        case "linux":
            const name = cp.execSync("hostnamectl --pretty").toString().trim();
            return name === "" ? os.hostname() : name;
        default:
            return os.hostname();
    }
}

export function getDeviceType(): string {
    const platform = os.platform();

    switch(platform) {
        case "win32":
            return "Windows";
        case "darwin":
            return "Mac OS";
        case "linux":
            return "linux";
        default:
            return "Unknown Device";
    }
}