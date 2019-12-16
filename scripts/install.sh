export HOME="/home/$SUDO_USER"
export CONF="/etc/unbound/unbound.conf.d/"
export SERVER_CONF="server.conf"

apt update && apt upgrade -y
apt install unbound -y
echo 'server:' > $CONF$SERVER_CONF
echo 'verbosity: 1' >> $CONF$SERVER_CONF
echo 'remote-control:' >> $CONF$SERVER_CONF
echo 'control-enable: yes' >> $CONF$SERVER_CONF
chown unbound $CONF
chmod -R 655 $CONF
systemctl restart unbound
