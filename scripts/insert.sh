#!/bin/bash

echo -e "\n*** Submitting requests to Menu Service ***\n"
printf "\n\n"
curl -X POST http://localhost:9091/menu-items/create/bread
printf "\n\n"
curl -X POST http://localhost:9091/menu-items/create/butter
printf "\n\n"
curl -X POST http://localhost:9091/menu-items/create/beer
printf "\n\n"

echo -e "\n*** Submitting requests for userone ***\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"

echo -e "\n*** Sleeping for 2 seconds ***"
sleep 2

echo -e "\n*** Submitting requests for userone and usertwo ***\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/usertwo
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/usertwo
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/usertwo
echo -e "\n*** Sleeping for 2 seconds ***"
sleep 2
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/usertwo
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/usertwo
printf "\n\n"
curl http://localhost:9091/menu-items/get/userone
printf "\n\n"
curl http://localhost:9091/menu-items/get/usertwo
printf "\n\n"


