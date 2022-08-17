APP_HOME=/opt/unlenen


echo "----------------------------------------"
echo "  Unlenen ONAP API Container: 1.0"
echo "  Author : unlenen@gmail.com"
echo "  Start Time : $(date)"
echo "  App Home: ${APP_HOME}" 
echo "  ONAP IP : ${ONAP_IP} "
echo "----------------------------------------"

cd $APP_HOME

java -jar onap-rest-1.0.jar
