function configurarMoeda(id) {
	$("#"+id).maskMoney({
		showSymbol : true,
		symbol : "R$ ",
		decimal : ",",
		thousands : ".",
		allowZero : true,
		symbolStay: true
	});
}


