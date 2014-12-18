#!/bin/bash
REPO_NAME=1.0.0-SNAPSHOT
echo "starting deploy..."
git config --global user.name ${GIT_USER}
git config --global user.email ${GIT_EMAIL}
rm -rf p2-repo
git clone --quiet https://github.com/SirWayne/SirWayne.github.io.git p2-repo 
echo cd p2-repo
cd p2-repo/ecp-vaadin-p2
mkdir -p $REPO_NAME
rm -rf $REPO_NAME/*
cp -r ../../releng/org.eclipse.emf.ecp.vaadin.releng.repository/target/repository/* ./$REPO_NAME
git add --all .
git commit -m "Deployed to Github P2"
echo Push
git push --force --quiet "https://${GH_TOKEN}@github.com/SirWayne/SirWayne.github.io.git" > /dev/null 2>&1
cd ..
echo Delete
rm -rf .git/credentials