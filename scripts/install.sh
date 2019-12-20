export HOME="/home/$SUDO_USER"
export CONF="/etc/unbound/unbound.conf.d/"
export SERVER_CONF="server.conf"
export CONTROL_CONF="remote.conf"
apt update && apt upgrade -y
apt install -y gnupg software-properties-common unbound mariadb-server
wget -O- https://apt.corretto.aws/corretto.key | sudo apt-key add -
add-apt-repository 'deb https://apt.corretto.aws stable main'
apt update && apt upgrade -y
apt install -y java-11-amazon-corretto-jdk
echo 'server:' > $CONF$SERVER_CONF
echo 'verbosity: 3' >> $CONF$SERVER_CONF
echo 'remote-control:' >> $CONF$CONTROL_CONF
echo 'control-enable: yes' >> $CONF$CONTROL_CONF
echo 'control-interface: 0.0.0.0' >> $CONF$CONTROL_CONF
echo 'control-use-cert:no' >> $CONF$CONTROL_CONF
chown unbound $CONF
chmod -R 755 $CONF
systemctl restart unbound
