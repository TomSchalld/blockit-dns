export HOME="/home/$SUDO_USER"
export USER_CONF="$HOME/.unbound.conf.d"

apt update && apt upgrade -y
apt install unbound -y
systemctl stop unbound
mkdir $USER_CONF
echo 'include: "'"$USER_CONF"'/*.conf"' > $USER_CONF/include.conf
mv $USER_CONF/include.conf /etc/unbound/unbound.conf.d/include.conf
chown -R $SUDO_USER $USER_CONF
