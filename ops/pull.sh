echo "Puling and deploying a new image of MediaWiki PHP side"
echo "Note, that it requires 'docker login' and previous image should be stopped."

echo "Tag: $1"

docker pull grenader/mw-mediawiki:$1

var=$(docker images | grep $1)

echo "Images search output: $var"
pat=$1' +(.*) +(.*)'
[[ "$var" =~ $pat ]]
var=${BASH_REMATCH[1]}
imageId=$(echo "${BASH_REMATCH[1]}" | sed 's/ .*//' | xargs)

echo "-------------------------------"
echo "Downloaded imageId: $imageId"

docker run --name new2mw6 --link mysql56 -p 80:80 -e MEDIAWIKI_SITE_SERVER=http://dev.wikivedas.com  -e MEDIAWIKI_ADMIN_PASS=11 -e MEDIAWIKI_DB_PASSWORD=11 -e MEDIAWIKI_DB_NAME=mw_db2 -e MEDIAWIKI_DB_USER=root -e MEDIAWIKI_DB_HOST=mysql56 -d $imageId
