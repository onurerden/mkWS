
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Flot Examples: Time zones</title>

    <style>

        * {	padding: 0; margin: 0; vertical-align: top; }

        body {
            background: url(background.png) repeat-x;
            font: 18px/1.5em "proxima-nova", Helvetica, Arial, sans-serif;
        }

        a {	color: #069; }
        a:hover { color: #28b; }

        h2 {
            margin-top: 15px;
            font: normal 32px "omnes-pro", Helvetica, Arial, sans-serif;
        }

        h3 {
            margin-left: 30px;
            font: normal 26px "omnes-pro", Helvetica, Arial, sans-serif;
            color: #666;
        }

        p {
            margin-top: 10px;
        }

        button {
            font-size: 18px;
            padding: 1px 7px;
        }

        input {
            font-size: 18px;
        }

        input[type=checkbox] {
            margin: 7px;
        }

        #header {
            position: relative;
            width: 900px;
            margin: auto;
        }

        #header h2 {
            margin-left: 10px;
            vertical-align: middle;
            font-size: 42px;
            font-weight: bold;
            text-decoration: none;
            color: #000;
        }

        #content {
            width: 880px;
            margin: 0 auto;
            padding: 10px;
        }

        #footer {
            margin-top: 25px;
            margin-bottom: 10px;
            text-align: center;
            font-size: 12px;
            color: #999;
        }

        .demo-container {
            box-sizing: border-box;
            width: 850px;
            height: 450px;
            padding: 20px 15px 15px 15px;
            margin: 15px auto 30px auto;
            border: 1px solid #ddd;
            background: #fff;
            background: linear-gradient(#f6f6f6 0, #fff 50px);
            background: -o-linear-gradient(#f6f6f6 0, #fff 50px);
            background: -ms-linear-gradient(#f6f6f6 0, #fff 50px);
            background: -moz-linear-gradient(#f6f6f6 0, #fff 50px);
            background: -webkit-linear-gradient(#f6f6f6 0, #fff 50px);
            box-shadow: 0 3px 10px rgba(0,0,0,0.15);
            -o-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            -ms-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            -moz-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            -webkit-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }

        .demo-placeholder {
            width: 100%;
            height: 100%;
            font-size: 14px;
            line-height: 1.2em;
        }

        .legend table {
            border-spacing: 5px;
        }
    </style>

    <!--[if lte IE 8]><script language="javascript" type="text/javascript" src="../../excanvas.min.js"></script><![endif]-->
    <script src="js/jquery-1.11.0.js"></script>
    <script src="js/plugins/flot/jquery.flot.js"></script>
    <script src="js/plugins/flot/jquery.flot.time.js"></script>
    <script src="js/plugins/flot/date.js"></script>


    <script type="text/javascript">

        $(function() {

            timezoneJS.timezone.zoneFileBasePath = "tz";
            timezoneJS.timezone.defaultZoneFile = 'europe';
            timezoneJS.timezone.init({async: false});

            var d = [
                [Date.UTC(2011, 2, 12, 14, 0, 0), 28],
                [Date.UTC(2011, 2, 12, 15, 0, 0), 27],
                [Date.UTC(2011, 2, 12, 16, 0, 0), 25],
                [Date.UTC(2011, 2, 12, 17, 0, 0), 19],
                [Date.UTC(2011, 2, 12, 18, 0, 0), 16],
                [Date.UTC(2011, 2, 12, 19, 0, 0), 14],
                [Date.UTC(2011, 2, 12, 20, 0, 0), 11],
                [Date.UTC(2011, 2, 12, 21, 0, 0), 9],
                [Date.UTC(2011, 2, 12, 22, 0, 0), 7.5],
                [Date.UTC(2011, 2, 12, 23, 0, 0), 6],
                [Date.UTC(2011, 2, 13, 0, 0, 0), 5],
                [Date.UTC(2011, 2, 13, 1, 0, 0), 6],
                [Date.UTC(2011, 2, 13, 2, 0, 0), 7.5],
                [Date.UTC(2011, 2, 13, 3, 0, 0), 9],
                [Date.UTC(2011, 2, 13, 4, 0, 0), 11],
                [Date.UTC(2011, 2, 13, 5, 0, 0), 14],
                [Date.UTC(2011, 2, 13, 6, 0, 0), 16],
                [Date.UTC(2011, 2, 13, 7, 0, 0), 19],
                [Date.UTC(2011, 2, 13, 8, 0, 0), 25],
                [Date.UTC(2011, 2, 13, 9, 0, 0), 27],
                [Date.UTC(2011, 2, 13, 10, 0, 0), 28],
                [Date.UTC(2011, 2, 13, 11, 0, 0), 29],
                [Date.UTC(2011, 2, 13, 12, 0, 0), 29.5],
                [Date.UTC(2011, 2, 13, 13, 0, 0), 29],
                [Date.UTC(2011, 2, 13, 14, 0, 0), 28],
                [Date.UTC(2011, 2, 13, 15, 0, 0), 27],
                [Date.UTC(2011, 2, 13, 16, 0, 0), 25],
                [Date.UTC(2011, 2, 13, 17, 0, 0), 19],
                [Date.UTC(2011, 2, 13, 18, 0, 0), 16],
                [Date.UTC(2011, 2, 13, 19, 0, 0), 14],
                [Date.UTC(2011, 2, 13, 20, 0, 0), 11],
                [Date.UTC(2011, 2, 13, 21, 0, 0), 9],
                [Date.UTC(2011, 2, 13, 22, 0, 0), 7.5],
                [Date.UTC(2011, 2, 13, 23, 0, 0), 6]
            ];

            var plot = $.plot("#placeholderUTC", [d], {
                xaxis: {
                    mode: "time",
                    timezone: "America/Chicago"
                    
                    
                }
            });



            // Add the Flot version string to the footer

            $("#footer").prepend("Flot " + $.plot.version + " &ndash; ");
        });

    </script>
</head>
<body>

<div id="header">
    <h2>Time zones</h2>
</div>

<div id="content">

    <h3>UTC</h3>
    <div class="demo-container" style="height: 300px; width:500px; ">
        <div id="placeholderUTC" class="demo-placeholder"></div>
    </div>

    <h3>Browser</h3>
    <div class="demo-container" style="height: 300px; width:500px;">
        <div id="placeholderLocal" class="demo-placeholder"></div>
    </div>

    <h3>Chicago</h3>
    <div class="demo-container" style="height: 300px; width:500px;">
        <div id="placeholderChicago" class="demo-placeholder"></div>
    </div>

</div>

<div id="footer">
    Copyright &copy; 2007 - 2014 IOLA and Ole Laursen
</div>

</body>
</html>
