#!/usr/bin/python3

import socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
try:
    s.bind(('', 0))
    addr = s.getsockname()
    print(addr[1])
    s.close()
except:
    print('NOT_FOUND')
    s.close()
