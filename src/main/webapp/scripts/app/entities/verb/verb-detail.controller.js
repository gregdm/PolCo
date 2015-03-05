'use strict';

angular.module('polcoApp')
    .controller('VerbDetailController', function ($scope, $stateParams, Verb, VerbTrans) {
        $scope.verb = {};
        $scope.load = function (id) {
            Verb.get({id: id}, function(result) {
              $scope.verb = result;
            });
        };
        $scope.load($stateParams.id);
    });
