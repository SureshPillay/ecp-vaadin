#!/bin/bash
echo "starting deploy..."
git config user.name SirWayne
git config user.email melzer.dennis@gmail.com
rm -rf p2-repo
git clone https://github.com/SirWayne/ecp-vaadin.git p2-repo 
cd p2-repo
rm -rf ecp-vaadin-p2/*
cd ecp-vaadin-p2
cp -r ../../releng/org.eclipse.emf.ecp.vaadin.releng.repository/target/repository/* .
git add --all .
git commit -m "Deployed to Github P2"
git push --force --quiet "https://${GH_TOKEN}@github.com/SirWayne/SirWayne.github.io.git"