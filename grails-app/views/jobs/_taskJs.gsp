<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'jquery.datetimepicker.css')}" />
<g:javascript src="jquery.js" />
<g:javascript src="jquery.datetimepicker.js" />
<script type="text/javascript">
    var j = jQuery.noConflict();
    j('document').ready(
            j('#datetimepicker').datetimepicker(
                    {
                        inline:true,
                        <g:if test="${!deadline.equals("")}">
                        value: '${deadline}',
                        format: 'Y/m/d H:i',
                        </g:if>
                        //minDate: 0,
                        step:30
                    }
            )
    );
</script>