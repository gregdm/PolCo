'use strict';

angular.module('polcoApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


