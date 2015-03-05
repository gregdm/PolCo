'use strict';

angular.module('polcoApp')
    .controller('VerbTradDetailController', function ($scope, $stateParams, VerbTrad, Verb) {
        $scope.verbTrad = {};
        $scope.load = function (id) {
            VerbTrad.get({id: id}, function(result) {
              $scope.verbTrad = result;
            });
        };
        $scope.load($stateParams.id);
    });
