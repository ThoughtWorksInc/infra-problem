#!/usr/bin/env python3

# From https://github.com/goj/cors-serve
import http.server

class CORSRequestHandler(http.server.SimpleHTTPRequestHandler):
    def end_headers(self):
        self.send_header('Access-Control-Allow-Origin', '*')
        super().end_headers()

if __name__ == '__main__':
    http.server.test(HandlerClass=CORSRequestHandler)