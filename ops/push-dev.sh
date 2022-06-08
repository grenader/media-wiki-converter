echo "Pushing a new image of Dev version MediaWiki PHP side"
echo "Note, that it requires 'docker login'"

[[ -z "$1" ]] && { echo "Parameter "container name``" (1) is empty" ; exit 1; }

OUTPUT="$(date +"Dev%Y%b%d%H")"
#echo "$OUTPUT" | tr '[:upper:]' '[:lower:]'
repoName=`echo "$OUTPUT" | tr '[:upper:]' '[:lower:]'`

echo RepoName: ${repoName}
echo Tag: ${OUTPUT}

docker commit $1 mw-mediawiki-${repoName}
docker tag mw-mediawiki-${repoName} grenader/mw-mediawiki:${OUTPUT}
docker push  grenader/mw-mediawiki:${OUTPUT}