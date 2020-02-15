recallCm <- function(cm, exp){
	sum = 0
	for(i in 1:exp){
		sum=sum+cm$byClass[(exp*5)+i]
	}
	return (sum/exp)
}

F1Cm <- function(cm,exp){
	sum = 0
	for(i in 1:exp){
		sum=sum+cm$byClass[(exp*6)+i]
	}
	return (sum/exp)
}

precisionCm <- function(cm, exp){
	sum = 0
	for(i in 1:exp){
		sum=sum+cm$byClass[(exp*4)+i]
	}
	return (sum/exp)
}	