'use strict';

angular.module('polcoApp')
    .controller('ImportController', function ($scope, $translate, $filter, AuditsService, Translation) {


        $scope.uploadFile = function(){
            var file = $scope.myFile;
            Translation.uploadFileToUrl(file)
                .success(function(data){

                                     })
                    //$scope.translatedText = data.value;})
                .error(function(data){
                    //$scope.translatedText = data;
                });
        };

        $scope.uploadExpression = function(){
            var file = $scope.myCSVExpression;
            Translation.uploadExpressionToUrl(file)
                .success(function(data){

                                     })
                   // $scope.translatedText = data.value;})
                .error(function(data){
                    //$scope.translatedText = data;
                });
        };

        $scope.uploadXMLFile = function(){
            var file = $scope.myFile;
            Translation.uploadFileXML(file)
                .success(function(data){
                                        //TODO GREG Handle error message
                                     })
                    //$scope.translatedText = data.value;})
                .error(function(data){
                    //$scope.translatedText = data;
                });
        };
    });
