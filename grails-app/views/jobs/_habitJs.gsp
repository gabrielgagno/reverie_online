<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'jquery.datetimepicker.css')}" />
<g:javascript src="jquery.js" />
<g:javascript src="jquery.datetimepicker.js" />
<script type="text/javascript">
    var j = jQuery.noConflict();
    j('document').ready(
            j('#rangeStart').datetimepicker(
                    {
                        minDate: 0,
                        datepicker: true,
                        timepicker: false,
                        format:'Y/m/d'
                    }
            ),
            j('#rangeEnd').datetimepicker(
                    {
                        minDate: 0,
                        datepicker: true,
                        timepicker: false,
                        format: 'Y/m/d'
                    }
            ),
            j('#startHour').datetimepicker(
                    {
                        minDate: 0,
                        datepicker: false,
                        step:30,
                        format: 'H:i'
                    }
            ),
            j('#endHour').datetimepicker(
                    {
                        minDate: 0,
                        datepicker: false,
                        step:30,
                        format: 'H:i'
                    }
            )
    );
</script>