'use strict';

angular.module('polcoApp')
    .controller('VerbTransDetailController', function ($scope, $stateParams, VerbTrans, Verb) {
        $scope.verbTrans = {};
        $scope.load = function (id) {
            VerbTrans.get({id: id}, function(result) {
              $scope.verbTrans = result;
            });
        };
        $scope.load($stateParams.id);
    });
