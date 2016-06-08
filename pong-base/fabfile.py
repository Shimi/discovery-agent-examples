from fabric.api import env, run, local, cd, put, warn_only
from time import gmtime, strftime

env.roledefs = {
    'dev': ['127.0.0.1:2111', '127.0.0.1:2112'],
    'test': ['localhost'],
    'production': ['']
} 

env.user = 'vagrant'
env.key_filename='~/.ssh/id_rsa'

app_name='pong'
deploy_dir='/u/' + app_name + '/releases'
release_link='/u/' + app_name + '/current'
home='/var/local/' + app_name
logs_dir='/var/log/' + app_name

def setup():
	local("echo running setup using %s" % env.key_filename)
	run("sudo mkdir -p %s" % deploy_dir)
	run("sudo mkdir %s && sudo chown -R %s:%s %s" % (logs_dir, env.user, env.user, logs_dir))
	run("sudo chmod g+w %s" % deploy_dir)
	run("sudo ln -s %s %s" % (release_link, home))
    
def deploy():
	release=strftime("%Y%m%d%H%M%S", gmtime())
	release_dir = deploy_dir + '/' + release
	lib_dir = release_dir + '/lib'
	
	run("sudo mkdir -p %s && sudo chown -R %s:%s %s" % (lib_dir, env.user, env.user, release_dir))
	run("sudo rm -f %s && sudo ln -s %s %s" % (release_link, release_dir, release_link))

	put('pong.upstart', '/etc/init/pong.conf', use_sudo=True)

	with cd(release_dir):
		put('run.sh', '.', mode=0700)
		put('target/dependency/*', 'lib')
		put('target/*jar', 'lib')
		run("sudo ln -s %s logs" % logs_dir)
		
	with warn_only():
		stop()
		
	start()
	
def clean():
	run("sudo rm -Rf /u/%s" % app_name)
	run("sudo rm -Rf /var/log/%s" % app_name)
	run("sudo rm -Rf %s" % release_link)
	run("sudo rm -Rf %s" % home)

def restart():
	run("sudo restart pong")
	
def stop():
	run("sudo stop pong")	
	
def start():
	run("sudo start pong")		
