'use strict';

angular.module('polcoApp')
    .controller('NounDetailController', function ($scope, $stateParams, Noun, NounTrans) {
        $scope.noun = {};
        $scope.load = function (id) {
            Noun.get({id: id}, function(result) {
              $scope.noun = result;
            });
        };
        $scope.load($stateParams.id);
    });
