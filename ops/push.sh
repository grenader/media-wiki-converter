echo "Pushing a new image of MediaWiki PHP side"
echo "Note, that it requires 'docker login'"
OUTPUT="$(date +"%Y%b%d%H")"
#echo "$OUTPUT" | tr '[:upper:]' '[:lower:]'
repoName=`echo "$OUTPUT" | tr '[:upper:]' '[:lower:]'`

echo RepoName: ${repoName}
echo Tag: ${OUTPUT}

docker commit new3mw3 mw-mediawiki-${repoName}
docker tag mw-mediawiki-${repoName} grenader/mw-mediawiki:${OUTPUT}
docker push  grenader/mw-mediawiki:${OUTPUT}