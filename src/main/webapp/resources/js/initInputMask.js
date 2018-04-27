function mascaraDtInput(id){
	var campo =$('#'+id); 
	campo.inputmask({
		"alias":"date",
		"placeholder":"__/__/____",
		"onincomplete":function(){
			//var id = $(this).attr('id');
			//var date = $('#'+id).val();
			var date = campo.val();
			var dt = [];
			var atual = new Date();
			if(date != ""){
				date = date.replace(/_/g,"");
				dt = date.split("/");
				if(dt[0].length > 1 && date.length == 4){
					if(dt[1] == ""){
						dt[1] = atual.getMonth()+1;
						dt[2] = atual.getFullYear();
					}
					dt[1] = dt[1]<10?"0"+dt[1]:dt[1];
					campo.val(dt[0]+'/'+dt[1]+'/'+dt[2]);
				}else if(dt[1].length > 1){
					dt[1] = dt[1]<10?"0"+dt[1]:dt[1];
					dt[2] = atual.getFullYear();
					campo.val(dt[0]+'/'+dt[1]+'/'+dt[2]);
				}else{
					campo.val('');
				}
			}
		}
	});
}


function mascaraDtCalendar(id){
	var campo =$('#'+id+"_input"); 
	campo.inputmask({
		"alias":"date",
		"placeholder":"__/__/____",
		"onincomplete":function(){
			//var id = $(this).attr('id');
			//var date = $('#'+id).val();
			var date = campo.val();
			var dt = [];
			var atual = new Date();
			if(date != ""){
				date = date.replace(/_/g,"");
				dt = date.split("/");
				if(dt[0].length > 1 && date.length == 4){
					if(dt[1] == ""){
						dt[1] = atual.getMonth()+1;
						dt[2] = atual.getFullYear();
					}
					dt[1] = dt[1]<10?"0"+dt[1]:dt[1];
					campo.val(dt[0]+'/'+dt[1]+'/'+dt[2]);
				}else if(dt[1].length > 1){
					dt[1] = dt[1]<10?"0"+dt[1]:dt[1];
					dt[2] = atual.getFullYear();
					campo.val(dt[0]+'/'+dt[1]+'/'+dt[2]);
				}else{
					campo.val('');
				}
			}
		}
	});
}

function mascaraMoeda(){
	$(".moeda").maskMoney({decimal: ",", thousands: ".", allowZero: true, prefix: "R$ "});
}

function formatarMoeda(comp){
	id = comp.id;
	$("#"+id).maskMoney({
		showSymbol : true,
		symbol : "R$ ",
		decimal : ",",
		thousands : ".",
		allowZero : true,
		symbolStay: true
	});
}

function formatoMoeda(id){
	$("#"+id).maskMoney({
		showSymbol : true,
		symbol : "R$ ",
		decimal : ",",
		thousands : ".",
		allowZero : true,
		symbolStay: true
	});
}

function formatoMoedaS(id){
	$("#"+id).maskMoney({
		showSymbol : true,
		symbol : "",
		decimal : ",",
		thousands : ".",
		allowZero : true,
		symbolStay: true
	});
}

