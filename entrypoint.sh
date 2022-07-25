APP_HOME=/opt/argela


echo "----------------------------------------"
echo "  Argela ONAP API Container: 1.0"
echo "  Author : nebi.unlenen@argela.com.tr"
echo "           caner.turkaslan.argela.com.tr"
echo "           hilal.alsac@argela.com.tr"
echo "  Start Time : $(date)"
echo "  App Home: ${APP_HOME}" 
echo "  ONAP IP : ${ONAP_IP} "
echo "----------------------------------------"

cd $APP_HOME

java -jar onap-rest-1.0.jar
