# From http://benlimmer.com/2013/12/26/automatically-publish-javadoc-to-gh-pages-with-travis-ci/

if [[ "$TRAVIS_REPO_SLUG" == "OliverAbdulrahim/ToStringHelper" && "$TRAVIS_JDK_VERSION" == "oraclejdk8" && "$TRAVIS_PULL_REQUEST" == "false" && "$TRAVIS_BRANCH" == "build" ]]; then
 
  echo -e "Publishing Javadoc...\n"

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet --branch=gh-pages https://${TOKEN}@github.com/OliverAbdulrahim/ToStringHelper gh-pages > /dev/null

  cd gh-pages
  git clean -d -x -f
  cp -Rf "$HOME/build/OliverAbdulrahim/ToStringHelper/target/site/apidocs/." "$PWD"
  git add -f .
  git commit -m "Lastest Javadoc on successful Travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published Javadoc to gh-pages.\n"

fi
