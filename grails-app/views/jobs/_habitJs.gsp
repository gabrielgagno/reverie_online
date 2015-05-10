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
</script>