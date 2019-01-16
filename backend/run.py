#!/usr/bin/env python
from flask import Flask, jsonify
from subprocess import call, Popen, PIPE


app = Flask(__name__)


@app.route('/mute')
def mute():
	call(['pactl', 'set-sink-mute', '0', 'toggle'])
	return ''


@app.route('/vol_down')
def vol_down():
	call(['pactl', 'set-sink-volume', '0', '-2%'])
	return 'vol down'


@app.route('/vol_up')
def vol_up():
	call(['pactl', 'set-sink-volume', '0', '+2%'])
	return 'vol up'


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


@app.route('/shuffle')
def shuffle():
	call(['cmus-remote', '-S'])
	return ''


@app.route('/query')
def query():
	p = Popen(['cmus-remote', '-Q'], stdout=PIPE, stderr=PIPE)
	output, err = p.communicate()
	output = output.decode()
	status = {}

	for line in output.split('\n'):
		if line == 'status playing':
			status['playing'] = True
		elif line == 'status paused':
			status['playing'] = False
		elif line.startswith('tag artist '):
			status['artist'] = line[11:]
		elif line.startswith('tag title '):
			status['title'] = line[10:]
		elif line == 'set shuffle true':
			status['shuffle'] = True
		elif line == 'set shuffle false':
			status['shuffle'] = False

	return jsonify(status)


if __name__ == '__main__':
	app.run(host='0.0.0.0')

