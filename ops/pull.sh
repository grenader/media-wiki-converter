echo "Pulling and deploying a new image of MediaWiki PHP side"
echo "Note, that it requires 'docker login' and previous image should be stopped."

[[ -z "$1" ]] && { echo "Parameter "image tag" (1) is empty" ; exit 1; }
[[ -z "$2" ]] && { echo "Parameter "new container name" (2) is empty" ; exit 2; }

read -p "Make sure you have stopped your current mediawiki image. Press enter to continue or cancel the script ..."

echo "Tag: $1, new contater name: $2"

docker pull grenader/mw-mediawiki:$1

var=$(docker images | grep $1)

echo "Images search output: $var"
pat=$1' +(.*) +(.*)'
[[ "$var" =~ $pat ]]
var=${BASH_REMATCH[1]}
imageId=$(echo "${BASH_REMATCH[1]}" | sed 's/ .*//' | xargs)

echo "-------------------------------"
echo "Downloaded imageId: $imageId"

docker run --name $2 --link mysql56 -p 80:80 -e MEDIAWIKI_SITE_SERVER=http://wikivedas.com  -e MEDIAWIKI_ADMIN_PASS=11 -e MEDIAWIKI_DB_PASSWORD=11 -e MEDIAWIKI_DB_NAME=mw_db3 -e MEDIAWIKI_DB_USER=root -e MEDIAWIKI_DB_HOST=mysql56 -d $imageId

