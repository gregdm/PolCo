'use strict';

angular.module('polcoApp')
    .controller('ImportController', function ($scope, $translate, $filter, AuditsService, ImportService) {


        $scope.exportTrad = function(){
            ImportService.exportTrad()
                .success(function(data, status, headers, config) {
                             var element = angular.element('<a/>');
                             element.attr({
                                              href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
                                              target: '_blank',
                                              download: 'traductions.csv'
                                          })[0].click();

                         }).
                error(function(data, status, headers, config) {
                          // if there's an error you should see it here
                      });
        };

        $scope.uploadFile = function(){
            var file = $scope.myFile;
            ImportService.uploadFileToUrl(file)
                .success(function(data){

                                     })
                    //$scope.translatedText = data.value;})
                .error(function(data){
                    //$scope.translatedText = data;
                });
        };

        $scope.uploadExpression = function(){
            var file = $scope.myCSVExpression;
            ImportService.uploadExpressionToUrl(file)
                .success(function(data){

                                     })
                   // $scope.translatedText = data.value;})
                .error(function(data){
                    //$scope.translatedText = data;
                });
        };

        $scope.uploadXMLFile = function(){
            var file = $scope.myFile;
            ImportService.uploadFileXML(file)
                .success(function(data){
                                        //TODO GREG Handle error message
                                     })
                    //$scope.translatedText = data.value;})
                .error(function(data){
                    //$scope.translatedText = data;
                });
        };
    });
