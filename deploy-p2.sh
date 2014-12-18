#!/bin/bash
echo `pwd`
echo "starting deploy..."
git config user.name SirWayne
git config user.email melzer.dennis@gmail.com
rm -rf p2-repo
git clone ssh://git@github.com/SirWayne/SirWayne.github.io.git p2-repo 
rm -rf ecp-vaadin-p2/*
cd ecp-vaadin-p2
echo `pwd`
cp releng/org.eclipse.emf.ecp.vaadin.releng.repository/* .
git add .
git commit -m "Deployed to Github P2"
git push --force --quiet "https://jjNqzbhMAh6/xGZS+l6TIArOsDQf2EhiC0pBKIUApRvu3zfsN9rlG47fX/ODhJ5xjhP33ycYNJYt+C5pJZf8R2GmX8jHF04RBQlnA5cM7Z/s2AxB1WnS/KquqIMqr1P0/sOIP8rPoCLhakW1KJhAgIJfEvzDVNtJIknn2Sf9FPE=@github.com/SirWayne/SirWayne.github.io.git" p2-repo > /dev/null 2>&1