echo 'Setting up environment'
export HOME="/home/$SUDO_USER/install"
export CONF="/etc/unbound/unbound.conf.d/"
export SERVER_CONF="server.conf"
export CONTROL_CONF="remote.conf"
export LOGGING_CONF="logging.conf"
mkdir -p $HOME
cd $HOME
wget -q https://raw.githubusercontent.com/TomSchalld/blockit-dns/alpha-0.0.1/scripts/blockit.service
wget -q https://github.com/TomSchalld/blockit-dns/releases/download/alpha-0.0.1/blockit.jar
echo 'Updating packages'
apt update && apt upgrade -y 
echo 'Packages upgraded'
echo 'Installing ACL'
apt install -y acl
setfacl -Rnm g:systemd-journal:rx,d:g:systemd-journal:rx /run/log/journal/
echo 'Installing Maria DB'
apt install -y gnupg software-properties-common unbound mariadb-server 
echo 'Maria DB install completed'
echo 'Installing Amazon Cornetto <3'
wget -O- https://apt.corretto.aws/corretto.key | apt-key add - 
add-apt-repository 'deb https://apt.corretto.aws stable main' 
apt update && apt upgrade -y 
apt install -y java-11-amazon-corretto-jdk 
echo 'Initializing basic conf files'
echo 'server:' > $CONF$SERVER_CONF
echo 'verbosity: 1' >> $CONF$SERVER_CONF
echo 'remote-control:' >> $CONF$CONTROL_CONF
echo 'control-enable: yes' >> $CONF$CONTROL_CONF
echo 'control-use-cert: no' >> $CONF$CONTROL_CONF
echo 'control-interface: 127.0.0.1' >> $CONF$CONTROL_CONF
echo 'server:' > $CONF$LOGGING_CONF
echo 'log-replies:yes' >> $CONF$LOGGING_CONF
echo 'log-tag-queryreply: yes' >> $CONF$LOGGING_CONF
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
usermod -a -G systemd-journal unbound
systemctl enable blockit 
systemctl restart unbound 
systemctl start blockit 
