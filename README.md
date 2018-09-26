This is a WORK IN PROGRESS

This consumes Bro, Suricata, and FSF events from RockNSM and sends
them to HACEP.

RockNSM VM config:  12 GB memory, 4 vCPUs, 256 GB storage, two
bridged network interfaces, one for management and one for monitoring.

Install RockNSM, create unpriviledged user and make them an
administrator, and accept license.  Do not do a yum update after
install.

After RockNSM is installed as a VM, do the following:

    cd /opt/rocknsm/rock/bin
    sudo ./generate_defaults.sh
    sudo ./deploy_rock.sh

    sudo -i
    yum -y update && yum -y clean all \
        && rm -fr /var/cache/yum && systemctl reboot

After the RockNSM VM is rebooted, do the following:

    sudo yum -y --enablerepo=base --enablerepo=updates \
        --enablerepo=extras install maven java-1.8.0-openjdk-devel

