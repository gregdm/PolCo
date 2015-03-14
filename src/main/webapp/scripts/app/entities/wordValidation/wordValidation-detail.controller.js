'use strict';

angular.module('polcoApp')
    .controller('WordValidationDetailController', function ($scope, $stateParams, WordValidation) {
        $scope.wordValidation = {};
        $scope.load = function (id) {
            WordValidation.get({id: id}, function(result) {
              $scope.wordValidation = result;
            });
        };
        $scope.load($stateParams.id);
    });
