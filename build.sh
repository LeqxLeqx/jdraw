#!/bin/bash
#* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
#*  jdraw: A pure Java 2d rendering library                                *
#*  Copyright (C) 2017 LeqxLeqx                                            *
#*                                                                         *
#*  This program is free software: you can redistribute it and/or modify   *
#*  it under the terms of the GNU General Public License as published by   *
#*  the Free Software Foundation, either version 3 of the License, or      *
#*  (at your option) any later version.                                    *
#*                                                                         *
#*  This program is distributed in the hope that it will be useful,        *
#*  but WITHOUT ANY WARRANTY; without even the implied warranty of         *
#*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the          *
#*  GNU General Public License for more details.                           *
#*                                                                         *
#*  You should have received a copy of the GNU General Public License      *
#*  along with this program.  If not, see <http://www.gnu.org/licenses/>.  *
#*                                                                         *
#* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


original_classpath=$CLASSPATH
export CLASSPATH=lib/jmath-1.5.1.jar


if [[ $1 == clean ]] ; then
  rm -rf bin
  rm -f jdraw-*.jar
  exit 0
fi

if [[ (-e bin) ]] ; then
  rm -r bin
fi

mkdir bin
if [[ $? != 0 ]] ; then
  echo Failed to create bin folter
  exit 1
fi


echo Building...

javac -s src -d bin `find . -name *.java`
if [[ $? != 0 ]] ; then
  echo Javac failed
  exit 2
fi

cd bin


echo 'Manifest-version: 1.0' > Manifest.txt
jar cfm jdraw.jar Manifest.txt `find . -name *.class`

echo 'import jdraw.JDraw; public class VersionGetter { public static void main(String[] args) { System.out.print(JDraw.getVersion()); } }' > VersionGetter.java
javac -cp .:jdraw.jar VersionGetter.java
if [[ $? != 0 ]] ; then
  echo Failed to aquire version
  exit 4
fi

VERSION=`java -cp .:jdraw.jar VersionGetter`
mv jdraw.jar ../jdraw-$VERSION.jar


cd ..

export CLASSPATH=$original_classpath;

echo Done
