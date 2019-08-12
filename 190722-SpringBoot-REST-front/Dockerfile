# base image
FROM node:12.2.0-alpine

ADD node_modules /app/node_modules
ADD public /app/public
ADD src /app/src
ADD package-lock.json /app
ADD package.json /app

# install and cache app dependencies
#COPY package.json /app/package.json
RUN npm install --silent
RUN npm install react-scripts@3.0.1 -g --silent

# start app
WORKDIR /app
CMD ["npm", "start"]