<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <title>Inicio</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/app.css"/>" rel="stylesheet">
    <link href="<c:url value="/js/css/jquery-ui.css"/>" rel="stylesheet">
    <link href="<c:url value="/js/css/timepicker.css"/>" rel="stylesheet">
</head>

<body>
<div class="container-fluid">

    <div class="row-fluid">

        <h1 class="title">Sistema de riego</h1>

        <div id="panels" class="row-fluid">

            <div class=".col-xs-12 col-md-6">

                <section id="states" class="mybox">

                    <a href="/"><img id="update" src="<c:url value="/img/icon-update.png"/>"></a>

                    <h2 class="title">Estados</h2>

                    <div>
                        <div class="field">
                            <p class="property">Estado general:</p>
                            <p class="state-${stateCode}">${stateMessage}</p>
                        </div>
                    </div>

                    <div>
                        <div class="field">
                            <p class="property">Humedad:</p>
                            <p>${humidityValue}</p>
                        </div>
                    </div>
                    <div>
                        <div class="field">
                            <p class="property">Temperatura del tanque:</p>
                            <p>${waterTempValue}</p>
                        </div>
                    </div>

                </section>

                <section id="irrigate" class="mybox">

                    <h2 class="title">Riegos</h2>

                    <div>
                        <button id="btn-fixed-irrigate" class="btn btn-success" name="Fijo" value="Fijo">Fijo</button>
                        <button id="btn-scheduled-irrigate" class="btn btn-warning" name="Periodico" value="Periodico">Periodico</button>
                    </div>

                    <div id="fixed-irrigate">

                        <form action="<c:url value="/config/fixedIrrigate"/>" method="post">

                            <label>
                                <h4>Fecha</h4>
                                <input name="datetime" class="datepicker" type="text"/>
                            </label>

                            <label>
                                <h4>Fertilizante</h4>
                                <input type="checkbox" name="fertilizer" value="on"/>
                            </label>

                            <label>
                                <h4>Porcentaje</h4>
                                <select name="percentage">
                                    <option value="10">10%</option>
                                    <option value="20">20%</option>
                                    <option value="30">30%</option>
                                    <option value="40">40%</option>
                                    <option value="50">50%</option>
                                    <option value="60">60%</option>
                                    <option value="70">70%</option>
                                    <option value="80">80%</option>
                                    <option value="90">90%</option>
                                    <option value="100">100%</option>
                                </select>
                            </label>

                            <input class="btn btn-success" type="submit" value="Añadir"/>

                        </form>

                    </div>

                    <div id="scheduled-irrigate">

                        <form action="<c:url value="/config/scheduledIrrigate"/>" method="post">

                            <label>
                                <h4>Hora</h4>
                                <input name="time" class="timepicker" type="text"/>
                            </label>

                            <label>
                                L
                                <input type="checkbox" id="schedule-monday" name="scheduleDay" value="MON"/>
                            </label>

                            <label>
                                M
                                <input type="checkbox" id="schedule-tuesday" name="scheduleDay" value="TUE"/>
                            </label>
                            <label>
                                X
                                <input type="checkbox" id="schedule-wednesday" name="scheduleDay" value="WED"/>
                            </label>
                            <label>
                                J
                                <input type="checkbox" id="schedule-thursday" name="scheduleDay" value="THU"/>
                            </label>
                            <label>
                                V
                                <input type="checkbox" id="schedule-friday" name="scheduleDay" value="FRI"/>
                            </label>
                            <label>
                                S
                                <input type="checkbox" id="schedule-saturday" name="scheduleDay" value="SAT"/>
                            </label>
                            <label>
                                D
                                <input type="checkbox" id="schedule-sunday" name="scheduleDay" value="SUN"/>
                            </label>

                            <label>
                                <h4>Fertilizante</h4>
                                <input type="checkbox" name="fertilizer" value="on"/>
                            </label>

                            <label>
                                <h4>Porcentaje</h4>
                                <select name="percentage">
                                    <option value="10">10%</option>
                                    <option value="20">20%</option>
                                    <option value="30">30%</option>
                                    <option value="40">40%</option>
                                    <option value="50">50%</option>
                                    <option value="60">60%</option>
                                    <option value="70">70%</option>
                                    <option value="80">80%</option>
                                    <option value="90">90%</option>
                                    <option value="100">100%</option>
                                </select>
                            </label>

                            <input class="btn btn-success" type="submit" value="Añadir"/>

                        </form>

                    </div>

                </section>

            </div>

            <div class=".col-xs-12 col-md-6">

                <section id="plotly" class="mybox">

                    <h2 class="title">Plotly</h2>

                    <div>
                        <div class="field">
                            <p class="property">Última actualización:</p>
                            <p class="last-update">
                                <c:choose>
                                    <c:when test="${not empty plottyLast}">${plottyLast}</c:when>
                                    <c:otherwise>Nunca</c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>

                    <div>
                        <div class="field"><p class="property">Gráfica:</p>
                            <c:if test="${not empty plottyUrl}">
                                <a class="btn btn-primary" href="${plottyUrl}" target="_blank">Ver en Plotly</a>
                            </c:if>
                        </div>
                    </div>

                    <c:if test="${not empty plottyUrl}">
                        <div class="plotly-graph">
                            <img class="plotly-graph" src="${plottyUrl}.png" />
                        </div>
                    </c:if>

                </section>

            </div>

            <div class="aRigth .col-xs-12 col-md-6">

                <section id="log" class="mybox">

                    <h2 class="title">Últimos eventos registrados</h2>

                    <ul>
                        <c:forEach var="entry" items="${log}">
                            <li>${entry}</li>
                        </c:forEach>
                    </ul>

                </section>

            </div>

        </div>

    </div>
    <!-- END ROW-FLUID -->
</div>
<!-- END CONTAINER-FLUID -->

<script type="text/javascript" src="<c:url value="/js/jquery-1.11.3.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-timepicker-addon.js"/>"></script>
<script type="text/javascript">
    $(function () {

        $('.datepicker').datetimepicker();
        $('.timepicker').timepicker();

        $('#btn-fixed-irrigate').click(function () {
            $('#fixed-irrigate').css("display", "block");
        });

        $('#btn-scheduled-irrigate').click(function () {
            $('#scheduled-irrigate').css("display", "block");
        });

    });
</script>
</body>
</html>