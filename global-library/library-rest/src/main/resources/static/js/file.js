$('.my').change(function() {
    if ($(this).val() !== '') $(this).prev().text('Selected file: ' + $('input[type=file]').val().split('\\').pop());
    else $(this).prev().text('Choose file');
});