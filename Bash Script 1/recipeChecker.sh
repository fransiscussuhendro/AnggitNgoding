#!/bin/sh
# This is a comment!
filename='/d/anggit/SinarmasCafeA/'$1

inventory=()
while read line; do

inventory+=("$line")
done < $filename
declare -i itr=0
declare -i itr2=0



if [ "$2" == "APPLE PIE" ]
then
   for i in "${inventory[@]}"
	do
			test1="$i"
			test1=${test1,,}
			test1=${test1::-1}
			
			#echo "$test1"
			if [ "$test1" = "apple" ]; then
				itr+=1
			fi
	done
	#echo "apple = $itr"
	if (($itr >= 3)); then
		echo "You shall have $2!"
	else
		echo "You shall not have $2!"
	fi
	
	


elif [ "$2" == "PINEAPPLE PIE" ]
then
   for i in "${inventory[@]}"
	do
			test1="$i"
			test1=${test1,,}
			test1=${test1::-1}
			
			#echo "$test1"
			if [ "$test1" = "pineapple" ]; then
				itr+=1
			fi
	done
	#echo "Pineapple = $itr"
	if (($itr >= 3)); then
		echo "You shall have $2!"
	else
		echo "You shall not have $2!"
	fi


elif [ "$2" == "FRUIT PARFAIT" ]
then
   for i in "${inventory[@]}"
	do
			test1="$i"
			test1=${test1,,}
			test1=${test1::-1}
			
			#echo "$test1"
			if [ "$test1" = "pineapple" ]; then
				itr+=1
			fi
	done
	#echo "Pineapple = $itr"
	for k in "${inventory[@]}"
	do
			test11="$k"
			test11=${test11,,}
			test11=${test11::-1}
			
			#echo "$test11"
			if [ "$test11" = "apple" ]; then
				itr2+=1
			fi
	done
	#echo "Apple = $itr2"
	
	if (($itr >= 2 && $itr2 >= 2)); then
		echo "You shall have $2!"
	else
		echo "You shall not have $2!"
	fi
else
echo "We do not have that on the menu"
fi

