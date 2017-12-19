function gerenciaTeclaEnter() {
	$(document).ready(function () {
		   $('input').keypress(function (e) {
		        var code = null;
		        code = (e.keyCode ? e.keyCode : e.which);                
		        return (code === 13) ? false : true;
		        
		   });
		   
			$('input[type=text]').keydown(function(e){
				//get the next index of text input element
				var next_idx = $('input[type=text]').index(this) + 1;
			 
				//get number of text input element in a html document
				var tot_idx = $('body').find('input[type=text]').length;
			 
				//enter button in ASCII code
				if(e.keyCode === 13){
					if(tot_idx === next_idx)
						//go to the first text element if focused in the last text input element
						$('input[type=text]:eq(0)').focus();
					else
						//go to the next text input element
						$('input[type=text]:eq(' + next_idx + ')').focus();
				}
			});
		});
	
}