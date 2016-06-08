require 'etc'

# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|

  config.vm.box = "hashicorp/precise32"
  id_rsa_ssh_key_pub = File.read(File.join(Dir.home, ".ssh", "id_rsa.pub"))

  # apt-get update all vms 
  config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get update
    sudo apt-get -y install htop libwww-perl
    echo '#{id_rsa_ssh_key_pub }' >> /home/vagrant/.ssh/authorized_keys
  SHELL

  config.vm.provider :virtualbox do |vb|
    vb.customize ["modifyvm", :id, "--memory", "400"]
    vb.customize ["modifyvm", :id, "--cpus", "1"]
    vb.customize ["modifyvm", :id, "--cpuexecutioncap", "70"]
  end

  config.vm.box_check_update = false

  (1..3).each do |i|
    config.vm.define "consul-server-#{i}" do |node|
      node.vm.hostname = "consul-server-#{i}" 
      node.vm.network "private_network", ip: "192.168.25.10#{i}"
      node.vm.network "forwarded_port", guest: 22, host: "210#{i}", id: 'ssh'

      node.vm.provision "chef_solo" do |chef|

        chef.add_recipe "consul"
      
        chef.json = {
          "consul" => {
            "config" => {
              "datacenter" => "us-east-1",
              "server" => true,
              "bind_addr" => "192.168.25.10#{i}",
              "start_join" => ["192.168.25.101", "192.168.25.102", "192.168.25.103"],
              "bootstrap_expect" => 3,
              "verify_incoming" => true,
              "verify_outgoing" => true
            },
            "ui_install_dir" => "/usr/local",
            "serve_ui" => true,
          }
        } 
      end
    end
  end

  config.vm.define "pong-service-1" do |node|
  node.vm.hostname = "pong-service-1"
    node.vm.network "private_network", ip: "192.168.25.111"
    node.vm.network "forwarded_port", guest: 22, host: 2111, id: 'ssh'
    
    node.vm.provision "chef_solo" do |chef|

      chef.add_recipe "consul"
      chef.add_recipe "java"
    
      chef.json = {
        "consul" => {
          "config" => {
            "bind_addr" => "192.168.25.111",
            "datacenter" => "us-east-1",
            "start_join" => ["192.168.25.101", "192.168.25.102", "192.168.25.103"],
            "verify_incoming" => true,
            "verify_outgoing" => true
          }
        },
        "java" => {
          "install_flavor" => "oracle",
          "jdk_version" => 8,
          "oracle" => {
            "accept_oracle_download_terms" => true
         }
        }
      }
    end

    node.vm.provision "shell", inline: <<-SHELL
      echo '{"service": {"name": "pong", "tags": ["jvm"], "port": 9877, "checks": [{ "name": "health","http": "http://localhost:9877/health", "interval": "10s", "timeout": "1s"}]}}' > /etc/consul/pong.json
      consul reload
    SHELL

  end

  config.vm.define "pong-service-2" do |node|
    node.vm.hostname = "pong-service-2"
    node.vm.network "private_network", ip: "192.168.25.112"
    node.vm.network "forwarded_port", guest: 22, host: 2112, id: 'ssh'
    
    node.vm.provision "chef_solo" do |chef|

      chef.cookbooks_path = ["~/chef/cookbooks", "~/chef/berks-cookbooks"]
      chef.data_bags_path = ["~/chef/chef-repo/data_bags"]
      chef.add_recipe "totango-consul"
      chef.add_recipe "consul-service"
      chef.add_recipe "java"
    
      chef.json = {
        "consul" => {
          "config" => {
            "bind_addr" => "192.168.25.112",
            "datacenter" => "us-east-1",
            "start_join" => ["192.168.25.101", "192.168.25.102", "192.168.25.103"],
            "verify_incoming" => true,
            "verify_outgoing" => true
          }
        },
        "java" => {
          "install_flavor" => "oracle",
          "jdk_version" => 8,
          "oracle" => {
            "accept_oracle_download_terms" => true
         }
        }
      }
    end

    node.vm.provision "shell", inline: <<-SHELL
      echo '{"service": {"name": "pong", "tags": ["jvm"], "port": 9877, "checks": [{ "name": "health","http": "http://localhost:9877/health", "interval": "10s", "timeout": "1s"}]}}' > /etc/consul/pong.json
      consul reload
    SHELL

  end

  config.vm.define "ping-service" do |node|
    node.vm.hostname = "ping"
    node.vm.network "private_network", ip: "192.168.25.121"
    node.vm.network "forwarded_port", guest: 22, host: 2121, id: 'ssh'
    
    node.vm.provision "chef_solo" do |chef|

      chef.cookbooks_path = ["~/chef/cookbooks", "~/chef/berks-cookbooks"]
      chef.data_bags_path = ["~/chef/chef-repo/data_bags"]
      chef.add_recipe "totango-consul"
      chef.add_recipe "java"
    
      chef.json = {
        "consul" => {
          "bind_addr" => "192.168.25.121",
          "datacenter" => "us-east-1",
          "start_join" => ["192.168.25.101", "192.168.25.102", "192.168.25.103"],
          "verify_incoming" => true,
          "verify_outgoing" => true
        },
        "java" => {
          "install_flavor" => "oracle",
          "jdk_version" => 8,
          "oracle" => {
            "accept_oracle_download_terms" => true
         }
        }
      }
    end

    node.vm.provision "shell", inline: <<-SHELL
      echo '{"service": {"name": "ping", "tags": ["jvm"], "port": 9876, "checks": [{ "name": "ing health","http": "http://localhost:9876/health", "interval": "10s", "timeout": "1s"}]}}' >/etc/consul/ping.json
      consul reload
    SHELL

  end


end
