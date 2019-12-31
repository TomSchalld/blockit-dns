echo 'Setting up environment'
export HOME="/home/$SUDO_USER/install"
export CONF="/etc/unbound/unbound.conf.d/"
export SERVER_CONF="server.conf"
export CONTROL_CONF="remote.conf"
echo 'Updating packages'
apt update && apt upgrade -y 
echo 'Packages upgraded'
echo 'Installing Maria DB'
apt install -y gnupg software-properties-common unbound mariadb-server 
echo 'Maria DB install completed'
echo 'Installing Amazon Cornetto <3'
wget -O- https://apt.corretto.aws/corretto.key | sudo apt-key add - 
add-apt-repository 'deb https://apt.corretto.aws stable main' 
apt update && apt upgrade -y 
apt install -y java-11-amazon-corretto-jdk 
echo 'Initializing basic conf files'
echo 'server:' > $CONF$SERVER_CONF
echo 'verbosity: 3' >> $CONF$SERVER_CONF
echo 'remote-control:' >> $CONF$CONTROL_CONF
echo 'control-enable: yes' >> $CONF$CONTROL_CONF
echo 'control-interface: 127.0.0.1' >> $CONF$CONTROL_CONF
echo 'control-use-cert:no' >> $CONF$CONTROL_CONF
chown unbound $CONF 
chmod -R 755 $CONF 
echo 'Setting up Database'
mysql -e "CREATE DATABASE blockit;
create user 'blockit' identified by 'blockit';
GRANT all on blockit.* to blockit;" 
echo 'Creating services'
chown unbound $HOME/blockit.jar 
chmod 755 $HOME/blockit.jar 
cp $HOME/blockit.jar /usr/sbin/blockit.jar 
cp $HOME/blockit.service /etc/systemd/system/blockit.service 
systemctl restart unbound 
systemctl start blockit 
