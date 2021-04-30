#!/bin/sh
# This is a comment!
filename='/d/anggit/SinarmasCafeB/'$1

order=()
notfulfill=()
notfulfillshowed=()
declare -i max=0
while read line; do
# reading each line
order+=("$line")
done < $filename

for i in "${order[@]}"
	do
		#echo "$i"
		test1="$i"
		test1=${test1,,}
		
		test2="$2"
		test2=${test2,,}
		if [[ $test1 == *"$test2"* && $test1 == *"$3"* && $test1 == *"false"* ]]; then
			#echo "$i"
			notfulfill+=("$i")
		fi		
done

#for j in "${notfulfill[@]}"
#	do
#		echo "$j"
#			
#done
IFS=$'\n'          
notfulfill=( $(printf "%s\n" ${notfulfill[*]} | sort -r ) ) 
declare -a notfulfill
#echo "------sorted------"
#for j in "${notfulfill[@]}"
#	do
#		echo "$j"
#			
#done

#echo "Jumlah not fulfill ${#notfulfill[@]}"

if ((${#notfulfill[@]} >= 3)); then
		max=3
		#echo "Max = $max"
	else
		max=${#notfulfill[@]}
		#echo "Max = $max"
fi
declare -i itr=0
for k in "${notfulfill[@]}"
	do
	#echo "$itr <= $max"
		if (($itr < $max)); then
		notfulfillshowed+=("$k")
	fi
	itr+=1		
done
#echo "------showed------"
for l in "${notfulfillshowed[@]}"
	do
		echo "$l"
			
done
