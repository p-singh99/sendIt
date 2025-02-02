export function isDev(): boolean {
    return process.env.NODE_ENV === 'dev';
}

export function getSubnet(ip: string): string {
    return ip.slice(0, ip.lastIndexOf('.'));
}