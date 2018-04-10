#!/usr/bin/env python
from flask import Flask
from subprocess import call


app = Flask(__name__)


@app.route('/mute')
def mute():
    call(['pactl', 'set-sink-mute', '0', 'toggle'])
    return ''


@app.route('/vol_down')
def vol_down():
    call(['pactl', 'set-sink-volume', '0', '-5%'])
    return ''


@app.route('/vol_up')
def vol_up():
    call(['pactl', 'set-sink-volume', '0', '+5%'])
    return ''


@app.route('/prev')
def prev():
    call(['cmus-remote', '-r'])
    return ''


@app.route('/play')
def play():
    call(['cmus-remote', '-u'])
    return ''


@app.route('/next')
def next():
    call(['cmus-remote', '-n'])
    return ''


if __name__ == '__main__':
    app.run()

