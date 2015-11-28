/**
 * Created by nilsraabe on 04.07.15.
 */

$(document).ready(function () {
    $('#datepicker').datepicker({
        format: "dd.mm.yyyy",
        startView: 2,
        orientation: "top right",
        autoclose: true,
        defaultViewDate: {year: 1980, month: 01, day: 01}
    });
});