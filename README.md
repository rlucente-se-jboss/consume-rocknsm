# Forward RockNSM Events to HACEP
This consumes Bro and Suricata events from [RockNSM](http://rocknsm.io/)
and sends them to HACEP for complex event processing.  The intention
is to leverage the highly available and scalable complex event
processing within the HACEP architecture to provide near real-time
decision augmentation and automation for defensive cyber operations.

## Configure the RockNSM VM
Configure a virtual machine for RockNSM with the following minimum attributes:

* 12 GB memory
* 4 vCPUs
* 256 GB storage
* network interface for management
* network interface for monitoring in raw socket mode

## Install RockNSM
Follow the [installation guide](https://rocknsm.gitbooks.io/rocknsm-guide/content/build/install.html)
to install RockNSM.  A brief summary of the installation steps
follow.  Download the RockNSM ISO image and use it to install a
virtual machine.  During the installation, create an unprivileged
user and make sure that user an administrator.  Also, accept the
license during the install.  Make sure to not do a yum update after
the install.

After RockNSM is installed and rebooted as a VM, do the following:

    cd /opt/rocknsm/rock/bin
    sudo ./generate_defaults.sh
    sudo ./deploy_rock.sh

    sudo -i
    yum -y update && yum -y clean all && \
        rm -fr /var/cache/yum && systemctl reboot

## Install HACEP on the RockNSM VM
After the RockNSM VM is rebooted, do the following:

    sudo yum -y --enablerepo=base --enablerepo=updates \
        --enablerepo=extras install \
        git java-1.8.0-openjdk-devel maven

Make sure that the local user maven `settings.xml` file refers to
the following Red Hat repositories:

* https://maven.repository.redhat.com/ga/
* https://maven.repository.redhat.com/earlyaccess/all/

Run the following commands to build the needed sofware:

    git clone https://github.com/rlucente-se-jboss/hacep-eap-helper.git
    cd hacep-eap-helper
    ./clean.sh && ./install.sh

    git clone https://github.com/rlucente-se-jboss/consume-rocknsm.git
    cd consume-rocknsm
    mvn clean install

## Launch RockNSM and HACEP
Run the following command to launch RockNSM:

    sudo rock_start

Run the following command in a separate terminal window to launch
AMQ:

    cd hacep-eap-helper
    ./start-amq.sh

Wait for AMQ to fully start.

Run the following commands in a separate terminal window to launch
the HACEP servers:

    cd hacep-eap-helper
    ./start-eap.sh

Wait for the two EAP servers to start.  You can use the following
command to check their status:

    cd hacep-eap-helper
    ./check-eap-status.sh

Run the following commands in a separate terminal window to bridge
RockNSM events to HACEP:

    cd consume-rocknsm
    mvn exec:java

## Fact and Rule Definitions
Example facts and rule definitions are at:

* [Bro Fact Model](https://raw.githubusercontent.com/rlucente-se-jboss/hacep/dco-hacep/hacep-examples/hacep-model/src/main/java/it/redhat/hacep/playground/rules/model/BroFact.java)
* [Suricata Fact Model](https://raw.githubusercontent.com/rlucente-se-jboss/hacep/dco-hacep/hacep-examples/hacep-model/src/main/java/it/redhat/hacep/playground/rules/model/SuricataFact.java)
* [Rules Definition](https://raw.githubusercontent.com/rlucente-se-jboss/hacep/dco-hacep/hacep-examples/hacep-rules/src/main/resources/rules/rocknsm-demo.drl)

