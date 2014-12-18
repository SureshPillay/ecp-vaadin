#!/bin/bash
echo "starting deploy..."
git config user.name ${GIT_USER}
git config user.email ${GIT_EMAIL}
rm -rf p2-repo
git clone --quiet https://github.com/SirWayne/SirWayne.github.io.git p2-repo 
echo cd p2-repo
cd p2-repo
rm -rf ecp-vaadin-p2/*
echo `pwd'
cd ecp-vaadin-p2
echo `pwd`
cp -r ../../releng/org.eclipse.emf.ecp.vaadin.releng.repository/target/repository/* .
git add --all .
git commit -m "Deployed to Github P2"
echo Push
git push --force --quiet "https://${GH_TOKEN}@github.com/SirWayne/SirWayne.github.io.git" > /dev/null 2>&1
cd ..
echo Delete
rm -rf .git/credentials