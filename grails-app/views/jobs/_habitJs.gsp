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
                        <g:if test="${!rangeStart.equals(null)}">
                        value: '${rangeStart}',
                        </g:if>
                        format:'Y-m-d'
                    }
            ),
            j('#rangeEnd').datetimepicker(
                    {
                        minDate: 0,
                        datepicker: true,
                        timepicker: false,
                        <g:if test="${!rangeEnd.equals(null)}">
                        value: '${rangeEnd}',
                        </g:if>
                        format: 'Y-m-d'
                    }
            ),
            j('#startHour').datetimepicker(
                    {
                        minDate: 0,
                        datepicker: false,
                        step:30,
                        <g:if test="${!start.equals(null)}">
                        value: '${start}',
                        </g:if>
                        format: 'H:i'
                    }
            ),
            j('#endHour').datetimepicker(
                    {
                        minDate: 0,
                        datepicker: false,
                        step:30,
                        <g:if test="${!end.equals(null)}">
                        value: '${end}',
                        </g:if>
                        format: 'H:i'
                    }
            )
    );
    j('document').ready(
            function(){
                if($("#frequency option:selected").val()!="WEEKLY"){
                    $('#sun').attr("disabled", true);
                    $('#mon').attr("disabled", true);
                    $('#tue').attr("disabled", true);
                    $('#wed').attr("disabled", true);
                    $('#thu').attr("disabled", true);
                    $('#fri').attr("disabled", true);
                    $('#sat').attr("disabled", true);
                }
                else{
                    $("#sun").removeAttr("disabled");
                    $("#mon").removeAttr("disabled");
                    $("#tue").removeAttr("disabled");
                    $("#wed").removeAttr("disabled");
                    $("#thu").removeAttr("disabled");
                    $("#fri").removeAttr("disabled");
                    $("#sat").removeAttr("disabled");
                }
            }
    );
    j('#frequency').change(function(){
        if($("#frequency option:selected").val()!="WEEKLY"){
            $('#sun').attr("disabled", true);
            $('#mon').attr("disabled", true);
            $('#tue').attr("disabled", true);
            $('#wed').attr("disabled", true);
            $('#thu').attr("disabled", true);
            $('#fri').attr("disabled", true);
            $('#sat').attr("disabled", true);
        }
        else{
            $("#sun").removeAttr("disabled");
            $("#mon").removeAttr("disabled");
            $("#tue").removeAttr("disabled");
            $("#wed").removeAttr("disabled");
            $("#thu").removeAttr("disabled");
            $("#fri").removeAttr("disabled");
            $("#sat").removeAttr("disabled");
        }
    });
</script>